import { twMerge } from "tailwind-merge";
import { tv } from "tailwind-variants";
import { motion } from "framer-motion";
import { AiOutlineClose } from "react-icons/ai";
import { forwardRef } from "react";
const modalVariants = tv({
  slots: {
    parrent:
      "fixed inset-0 z-[99] flex items-end sm:items-center justify-center bg-black/50",
    children:
      "flex flex-col bg-stone-50 border border-gray-500 shadow-lg p-3 md:p-5 w-full sm:max-w-md md:max-w-lg lg:max-w-xl xl:max-w-2xl rounded-t-lg sm:rounded-lg",
  },
  variants: {
    mobileSize: {
      small: {
        children: "h-[40%]",
      },
      default: {
        children: "h-[80%]",
      },
      full: {
        children: "h-screen",
      },
    },
  },
});
interface ModalProps {
  children: React.ReactNode;
  mobileSize: "small" | "default" | "full" | undefined;
  classNameParrent?: string;
  classNameChildren?: string;
  title?: string;
  closeHandler?: () => void;
}
const Modal = forwardRef<HTMLDivElement, ModalProps>(
  (
    {
      children,
      mobileSize,
      classNameParrent,
      classNameChildren,
      title,
      closeHandler,
    },
    ref
  ) => {
    const { parrent, children: childrenClass } = modalVariants({ mobileSize });
    return (
      <div className={twMerge(parrent(), classNameParrent)}>
        <motion.div
          className={twMerge(childrenClass(), classNameChildren)}
          initial={{ opacity: 0, y: 1000 }}
          animate={{ opacity: 1, y: 0 }}
          exit={{ opacity: 0, y: 1000 }}
          transition={{
            duration: 0.6,
            ease: "easeInOut",
          }}
          ref={ref}
        >
          <div className="relative text-black text-center">
            <button
              className="absolute top-0 left-0 bg-gray-200 p-1.5 rounded-full sm:p-3 hover:scale-105 hover:border border-gray-300"
              onClick={closeHandler}
            >
              <AiOutlineClose />
            </button>
            <h2>{title}</h2>
            <div className="w-[90%] mx-auto h-[2px] bg-black mb-6 mt-6"></div>
          </div>
          {children}
        </motion.div>
      </div>
    );
  }
);

export default Modal;
