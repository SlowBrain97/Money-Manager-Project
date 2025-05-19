import { useEffect, useState, useRef } from "react";
import { LoggerMessage, OEM, Worker, createWorker } from "tesseract.js";
import { AlertCircle } from "lucide-react";
import { Alert, AlertDescription, AlertTitle } from "../ui/alert";
import Button from "../custom-ui/Button";
import { AiOutlineClose } from "react-icons/ai";
import { loadOpenCV, getCV } from "@/utils/OpenCv";
import { OrcState } from "@/utils/StateConfig";

interface CameraProps {
  handlerCamera: () => void;
}

const Camera = ({ handlerCamera }: CameraProps) => {
  const setOcrResult = OrcState((state) => state.setOrcResult);
  const videoRef = useRef<HTMLVideoElement>(null);
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const [imageData, setImageData] = useState<string>("");
  const [hasCamera, setHasCamera] = useState<boolean>(false);
  const [isRecapture, setIsRecapture] = useState<boolean>(false);
  const [isError, setIsError] = useState<string>("");
  const [isStreaming, setIsStreaming] = useState<boolean>(false);
  const [isOcrProcessing, setOrcProcessing] = useState<boolean>(false);
  const [processing, setProcessing] = useState<number>(0);
  const [isOpenCVLoaded, setIsOpenCVLoaded] = useState(false);

  useEffect(() => {
    // Load OpenCV when component mounts
    loadOpenCV()
      .then(() => {
        setIsOpenCVLoaded(true);
      })
      .catch((error) => {
        console.error("Error loading OpenCV:", error);
      });
  }, []);

  useEffect(() => {
    const video = videoRef.current;
    const startCamera = async () => {
      try {
        const stream: MediaStream = await navigator.mediaDevices.getUserMedia({
          video: { facingMode: "enviroment" },
          audio: false,
        });
        if (video && stream) {
          video.srcObject = stream;
          setHasCamera(true);
          setIsStreaming(true);
        }
      } catch (error) {
        console.log("Device have no camera or permission denied" + error);
        setIsError("Device have no camera or permission denied");
      }
    };

    startCamera();
    return () => {
      if (video && video.srcObject) {
        const stream = video.srcObject as MediaStream;
        stream.getTracks().forEach((track) => track.stop());
        setIsStreaming(false);
        console.log("track stoped");
      }
    };
  }, [isRecapture]);

  const captureImage = () => {
    if (!videoRef.current || !canvasRef.current) return;
    const canvas = canvasRef.current;
    const video = videoRef.current;
    canvas.width = video.videoWidth;
    canvas.height = video.videoHeight;

    const context = canvas.getContext("2d", { willReadFrequently: true });
    if (!context) return;
    context.drawImage(video, 0, 0, canvas.width, canvas.height);

    // Process with OpenCV
    try {
      const cv = getCV();
      const src = cv.imread(canvas);
      const dst = new cv.Mat();
      cv.cvtColor(src, dst, cv.COLOR_RGBA2GRAY);

      const ksize = new cv.Size(5, 5);
      cv.GaussianBlur(dst, dst, ksize, 0);

      cv.adaptiveThreshold(
        dst,
        dst,
        255,
        cv.ADAPTIVE_THRESH_GAUSSIAN_C,
        cv.THRESH_BINARY,
        11,
        2
      );

      const contours = new cv.MatVector();
      const hierarchy = new cv.Mat();
      cv.findContours(
        dst,
        contours,
        hierarchy,
        cv.RETR_EXTERNAL,
        cv.CHAIN_APPROX_SIMPLE
      );
      let maxArea = 0;
      let maxContourIndex = -1;

      for (let i = 0; i < contours.size(); i++) {
        const contour = contours.get(i);
        const area = cv.contourArea(contour);
        if (area > maxArea) {
          maxArea = area;
          maxContourIndex = i;
        }
      }

      if (maxContourIndex !== -1) {
        const mask = cv.Mat.zeros(dst.rows, dst.cols, cv.CV_8UC1);
        const color = new cv.Scalar(255, 255, 255);

        cv.drawContours(mask, contours, maxContourIndex, color, -1);
        const result = new cv.Mat();
        const originalImage = cv.imread(canvas);
        originalImage.copyTo(result, mask);

        result.convertTo(result, -1, 1.5, 0);

        cv.imshow(canvas, result);

        mask.delete();
        result.delete();
        originalImage.delete();
      } else {
        const enhanced = cv.imread(canvas);
        enhanced.convertTo(enhanced, -1, 1.5, 0);
        cv.cvtColor(enhanced, enhanced, cv.COLOR_RGBA2GRAY);
        cv.adaptiveThreshold(
          enhanced,
          enhanced,
          255,
          cv.ADAPTIVE_THRESH_GAUSSIAN_C,
          cv.THRESH_BINARY,
          11,
          2
        );
        cv.imshow(canvas, enhanced);
        enhanced.delete();
      }

      const imageDataUrl = canvas.toDataURL("image/png");
      setImageData(imageDataUrl);

      src.delete();
      dst.delete();
      contours.delete();
      hierarchy.delete();
    } catch (err) {
      console.error("Error processing with OpenCV:", err);
      context.filter = "contrast(150%) grayscale(100%)";
      context.drawImage(video, 0, 0, canvas.width, canvas.height);
      const imageDataUrl = canvas.toDataURL("image/png");
      setImageData(imageDataUrl);
    }
  };

  const processOCR = async () => {
    try {
      setOrcProcessing(true);
      const OcrWorker: Worker = await createWorker("jpn", OEM.DEFAULT, {
        logger: (m: LoggerMessage) => {
          if (m.status === "recognizing text") setProcessing(m.progress * 100);
        },
      });
      const {
        data: { text },
      } = await OcrWorker.recognize(imageData);
      setOcrResult(text);
      await OcrWorker.terminate();
    } catch (error) {
      console.log("cannot orc" + error);
      setIsError("Cannot Recognize this image" + error);
    } finally {
      setOrcProcessing(false);
    }
  };

  const handleRecapture = () => {
    setImageData("");
    setIsRecapture(!isRecapture);
  };

  if (isError) {
    return (
      <Alert variant="destructive">
        <AlertCircle className="h-4 w-4" />
        <AlertTitle>Error</AlertTitle>
        <AlertDescription>{isError}</AlertDescription>
      </Alert>
    );
  }
  return (
    <div className="fixed inset-0 w-screen z-[50]">
      <div className="relative w-full h-full">
        <button
          className="absolute top-4 left-4 bg-gray-200 p-1.5 rounded-full sm:p-3 hover:scale-105 hover:border border-gray-300 z-[999]"
          onClick={handlerCamera}
        >
          <AiOutlineClose />
        </button>
        {!hasCamera && (
          <Alert variant="destructive">
            <AlertCircle className="h-4 w-4" />
            <AlertTitle>Error</AlertTitle>
            <AlertDescription>
              Application has no permission for accessing camera
            </AlertDescription>
          </Alert>
        )}
        {imageData ? (
          <>
            <img src={imageData} className="w-full h-full" />
            <div className="absolute bottom-5 m-auto flex justify-around items-center gap-x-5 z-[999] left-0 right-0">
              <Button variant="circle" size="lg" onClick={handleRecapture}>
                Recapture
              </Button>
              <Button variant="circle" size="lg" onClick={processOCR}>
                Confirm
              </Button>
            </div>
          </>
        ) : (
          <>
            <video width="100%" height="100%" autoPlay ref={videoRef} />
            <Button
              variant="circle"
              size="lg"
              onClick={captureImage}
              className="absolute bottom-5 m-auto z-[999] left-0 right-0"
            >
              Capture
            </Button>
          </>
        )}
      </div>
      {imageData && <img src={imageData} alt="Error" />}
      <canvas ref={canvasRef} className="hidden"></canvas>
    </div>
  );
};

export default Camera;
