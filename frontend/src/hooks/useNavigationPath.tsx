import { useRef } from "react";
import { useLocation, useNavigate } from "react-router-dom";

const history: string[] = [];

const useNavigationPath = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const moveToPath = (path: string) => {
    history.push(path);
    navigate(path);
  };

  const goPreviousPage = () => {
    history.pop();
    navigate(-1);
  };
  return {
    navigate,
    location,
    moveToPath,
    goPreviousPage,
    history,
  };
};
export default useNavigationPath;
