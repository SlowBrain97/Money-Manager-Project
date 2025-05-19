import { motion } from "framer-motion";
import React from "react";

const variants = {
  initial: {
    x: "100%",
    opacity: 0,
  },
  animate: {
    x: 0,
    opacity: 1,
    transition: { duration: 0.8 },
  },
  exit: {
    x: "-100%",
    opacity: 0,
    transition: { duration: 0.8 },
  },
};

const SlideAnimation = ({ children }: { children: React.ReactNode }) => {
  return (
    <motion.div
      variants={variants}
      initial="initial"
      animate="animate"
      exit="exit"
      className="absolute w-full"
    >
      {children}
    </motion.div>
  );
};

export default SlideAnimation;
