import React, { useEffect, useRef, useState } from "react";
import { cva, type VariantProps } from "class-variance-authority";
import { twMerge } from "tailwind-merge";

// Define button variants using cva
const buttonVariants = cva(
  "flex items-center justify-center font-medium transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-blue-500 focus-visible:ring-offset-2 disabled:opacity-50 disabled:pointer-events-none relative hover:scale-105 duration-100 transition-transform ease-out",
  {
    variants: {
      variant: {
        default: "bg-black text-white rounded-2xl",
        circle: "rounded-full bg-white text-black",
        serrated: "bg-transparent text-white rounded-2xl",
      },
      size: {
        default: "h-16 w-24",
        sm: "h-12 w-20",
        lg: "h-20 w-20",
      },
    },
    defaultVariants: {
      variant: "default",
      size: "default",
    },
  }
);

interface ButtonProps
  extends React.ButtonHTMLAttributes<HTMLButtonElement>,
    VariantProps<typeof buttonVariants> {
  className?: string;
  teethCount?: number;
  teethDepth?: number;
  svGBorderColor?: string;
  circle?: boolean;
  svgBackgroundClass?: string;
}

const Button = React.forwardRef<HTMLButtonElement, ButtonProps>(
  (
    {
      className,
      variant,
      size,
      children,
      teethCount = 40, // Number of teeth around the circle
      teethDepth = 6, // Depth of teeth in pixels
      svGBorderColor = "",
      svgBackgroundClass = "",
      ...props
    },
    ref
  ) => {
    const buttonRef = useRef<HTMLButtonElement>(null);
    const [sizeSerated, setSizeSerated] = useState({ width: 100, height: 100 });

    useEffect(() => {
      const updateSize = () => {
        if (buttonRef.current) {
          const { width, height } = buttonRef.current.getBoundingClientRect();
          setSizeSerated({ width, height });
        }
      };

      updateSize();
      window.addEventListener("resize", updateSize);
      return () => window.removeEventListener("resize", updateSize);
    }, []);

    const generateSerratedPath = () => {
      const centerX = sizeSerated.width / 2;
      const centerY = sizeSerated.height / 2;
      const radius = Math.min(centerX, centerY);
      let path = "";

      for (let i = 0; i < teethCount; i++) {
        const angle1 = (i / teethCount) * 2 * Math.PI;
        const angle2 = ((i + 0.5) / teethCount) * 2 * Math.PI;
        const angle3 = ((i + 1) / teethCount) * 2 * Math.PI;

        const outerRadius = radius;
        const innerRadius = radius - teethDepth;

        const x1 = centerX + outerRadius * Math.cos(angle1);
        const y1 = centerY + outerRadius * Math.sin(angle1);

        const x2 = centerX + innerRadius * Math.cos(angle2);
        const y2 = centerY + innerRadius * Math.sin(angle2);

        const x3 = centerX + outerRadius * Math.cos(angle3);
        const y3 = centerY + outerRadius * Math.sin(angle3);

        if (i === 0) {
          path += `M ${x1},${y1} `;
        }

        path += `L ${x2},${y2} L ${x3},${y3} `;
      }

      path += "Z";
      return path;
    };

    return (
      <button
        className={twMerge(buttonVariants({ variant, size, className }))}
        ref={ref}
        {...props}
      >
        {variant === "serrated" && (
          <svg
            className="absolute top-0 left-0 w-full h-full pointer-events-none"
            viewBox={`0 0 ${sizeSerated.width} ${sizeSerated.height}`}
            preserveAspectRatio="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path d={generateSerratedPath()} fill="currentColor" />
            <path
              d={generateSerratedPath()}
              fill={svgBackgroundClass}
              stroke={svGBorderColor}
              strokeWidth="3"
            />
          </svg>
        )}
        <div className="z-50">{children}</div>
      </button>
    );
  }
);

Button.displayName = "Button";
export default Button;
// Demo component showing the serrated button
