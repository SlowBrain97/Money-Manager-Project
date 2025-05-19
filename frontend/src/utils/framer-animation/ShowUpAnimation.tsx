import React from "react";
import { motion } from "framer-motion";
const variants = {
  initial: {
    y: 20,
    opacity: 0,
  },
  animate: {
    y: 0,
    opacity: 1,
    transition: { duration: 0.3 },
  },
};
const ShowUpAnimation = ({ children }: { children: React.ReactNode }) => {
  return (
    <motion.div variants={variants} initial="initial" animate="animate">
      {children}
    </motion.div>
  );
};

export default ShowUpAnimation;
