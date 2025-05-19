import { twMerge } from "tailwind-merge";

const Layout = ({
  children,
  className,
}: {
  children: React.ReactNode;
  className?: string;
}) => {
  return (
    <div
      className={twMerge(
        "min-h-0 flex flex-col gap-y-2 container-responsive",
        className
      )}
    >
      {children}
    </div>
  );
};

Layout.Header = ({
  children,
  className,
}: {
  children: React.ReactNode;
  className?: string;
}) => {
  return <header className={className}>{children}</header>;
};
Layout.Body = ({
  children,
  className,
}: {
  children: React.ReactNode;
  className?: string;
}) => {
  return <main className={className}>{children}</main>;
};
Layout.Sider = ({
  children,
  className,
}: {
  children: React.ReactNode;
  className?: string;
}) => {
  return <aside className={className}>{children}</aside>;
};

Layout.Footer = ({
  children,
  className,
}: {
  children: React.ReactNode;
  className?: string;
}) => {
  return <footer className={className}>{children}</footer>;
};
export default Layout;
