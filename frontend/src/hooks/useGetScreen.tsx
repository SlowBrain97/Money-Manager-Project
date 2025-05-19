import { useEffect, useState } from "react";

const useGetScreen = () => {
  const [screen, setScreen] = useState<string>("");
  useEffect(() => {
    const handleResize = () => {
      if (window.innerWidth > 768) {
        setScreen("desktop");
      } else {
        setScreen("mobile");
      }
    };
    window.addEventListener("resize", handleResize);
    return () => {
      window.removeEventListener("resize", handleResize);
    };
  }, [screen]);

  return { screen };
};

export default useGetScreen;
