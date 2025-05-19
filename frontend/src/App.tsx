import HomePage from "./pages/HomePage";
import { FaRegBell } from "react-icons/fa";
import { LuAlignCenter } from "react-icons/lu";
import useGetScreen from "@/hooks/useGetScreen";
import { Route, Routes } from "react-router-dom";
import Layout from "./components/monocules/Layout";
import { Avatar, AvatarFallback, AvatarImage } from "./components/ui/avatar";
import Footer from "./components/monocules/Footer";
import { lazy, Suspense } from "react";
import Loading from "./components/custom-ui/Loading";
import { AnimatePresence } from "framer-motion";
import SlideAnimation from "./utils/framer-animation/SlideAnimation";
import ShowUpAnimation from "./utils/framer-animation/ShowUpAnimation";
import { ArrowLeftCircleIcon } from "lucide-react";
import useNavigationPath from "./hooks/useNavigationPath";

const BillsPage = lazy(() => import("./pages/BillsPage"));
function App() {
  const { screen } = useGetScreen();
  const { history, location, goPreviousPage } = useNavigationPath();
  return (
    <div className="flex-col min-h-screen">
      <Layout className="container-responsive gap-y-1.5">
        <ShowUpAnimation>
          <Layout.Header>
            <div
              className={`flex justify-between items-center mb-2 bg-color-yellow rounded-b-lg p-responsive-padding text-responsive-h3 gap-y-1.5 ${
                screen === "mobile" && "pt-[15%]"
              }`}
            >
              {history.length > 0 ? (
                <div className="h-15 relative z-0">
                  <div
                    className="relative z-20 flex gap-1 justify-center items-center hover-scale border-black p-3"
                    onClick={() => goPreviousPage()}
                  >
                    <ArrowLeftCircleIcon />
                    <span>Go Back</span>
                  </div>
                </div>
              ) : (
                <div className="flex gap-1 justify-center items-center ">
                  <Avatar>
                    <AvatarImage
                      src="https://github.com/shadcn.png"
                      alt="avatar"
                    />
                    <AvatarFallback>CN</AvatarFallback>
                  </Avatar>
                  <p className="text-xl font-semibold">Oliniva</p>
                </div>
              )}

              <div className="flex gap-x-2">
                <FaRegBell />
                <LuAlignCenter />
              </div>
            </div>
          </Layout.Header>
        </ShowUpAnimation>

        <Layout.Body className="flex-1">
          <AnimatePresence mode="popLayout">
            <SlideAnimation key={location.pathname}>
              <Suspense fallback={<Loading />}>
                <Routes location={location}>
                  <Route path="/" element={<HomePage />} />
                  <Route path="/bills" element={<BillsPage />}></Route>
                </Routes>
              </Suspense>
            </SlideAnimation>
          </AnimatePresence>
        </Layout.Body>

        <Layout.Footer>
          <Footer />
        </Layout.Footer>
      </Layout>
    </div>
  );
}

export default App;
