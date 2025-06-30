import { useState } from "react";
import SerratedButton from "@/components/custom-ui/Button";
import { GoChecklist } from "react-icons/go";
import Modal from "../custom-ui/Modal";
import { AnimatePresence } from "framer-motion";
import { FaWpforms } from "react-icons/fa6";
import { FaCamera } from "react-icons/fa";
import Camera from "./Camera";
import CustomForm from "../custom-ui/CustomForm";
import { BillFormContent } from "@/utils/StoreOfFormSchema";
import { Bill, db } from "@/utils/storeDb";
import TableWithPagination from "../custom-ui/CustomTable";
import useNavigationPath from "@/hooks/useNavigationPath";

const { defaultValue, billFields, billSchema } = BillFormContent();

const ButtonComponent = () => {
  const { moveToPath } = useNavigationPath();
  const [isCreateModalOpen, setCreateModalOpen] = useState(false);
  const [isCameraOn, setCameraOn] = useState(false);
  const [isBillFormOpen, setBillFormOpen] = useState(false);
  const [isThisMonthOpen, setThisMonthOpen] = useState(false);

  // const bills = useLiveQuery<Bill[]>(() => db.getAllBillsOfThisMonth(), []);
  const handleBillFormSubmit = async (bill: Bill) => {
    await db.addBillForThisMonth(bill);
    setBillFormOpen(!isBillFormOpen);
  };

  const handleCreateClick = () => {
    setCreateModalOpen(!isCreateModalOpen);
  };
  const handleCameraOn = () => {
    setCameraOn(!isCameraOn);
    setCreateModalOpen(false);
  };

  const handleBillFormOn = () => {
    setBillFormOpen(!isBillFormOpen);
    setCreateModalOpen(false);
  };

  const handleThisMonthClick = () => {
    setThisMonthOpen(!isThisMonthOpen);
    setCreateModalOpen(false);
  };

  return (
    <>
      <div className="flex justify-evenly gap-x-[calc(8%/3)]">
        <SerratedButton
          size="lg"
          variant="serrated"
          svgBackgroundClass="#036ac9"
          className="basis-1/3 aspect-square rounded-full"
          onClick={() => handleCreateClick()}
        >
          <div className="flex flex-col items-center">
            <h2>+</h2>
            <h3>Create</h3>
          </div>
        </SerratedButton>
        <SerratedButton
          size="lg"
          variant="circle"
          className="basis-1/3 aspect-square"
          onClick={handleThisMonthClick}
        >
          <div className="flex flex-col items-center">
            <GoChecklist />
            <p>This Month</p>
          </div>
        </SerratedButton>
        <SerratedButton
          size="lg"
          variant="circle"
          className="basis-1/3 aspect-square"
        >
          <div
            className="flex flex-col items-center"
            onClick={() => moveToPath("/bills")}
          >
            <GoChecklist />
            <span>Search Bill All Time</span>
          </div>
        </SerratedButton>
      </div>
      <AnimatePresence>
        {isCreateModalOpen && (
          <Modal
            mobileSize="small"
            title="Select method to save bill"
            closeHandler={handleCreateClick}
          >
            <div className="flex flex-col gap-y-4 justify-center items-center h-full">
              <button
                className="bg-transparent hover:scale-105 hover:border border-slate-700 px-3 py-1 rounded-xl flex justify-center items-center gap-x-2"
                onClick={handleBillFormOn}
              >
                Add bill by form
                <span className="inline-block">
                  <FaWpforms />
                </span>
              </button>
              <button
                className="bg-transparent hover:scale-105 hover:border border-slate-700 px-3 py-1 rounded-xl flex justify-center items-center gap-x-2"
                onClick={handleCameraOn}
              >
                Add bill by camera
                <span className="inline-block">
                  <FaCamera />
                </span>
              </button>
            </div>
          </Modal>
        )}

        {isCameraOn && <Camera handlerCamera={handleCameraOn} />}
        {isBillFormOpen && (
          <Modal
            title="Bill Form"
            mobileSize="full"
            closeHandler={handleBillFormOn}
          >
            <CustomForm
              defaultValue={defaultValue}
              fields={billFields}
              formSchema={billSchema}
              handleSubmit={handleBillFormSubmit}
            />
          </Modal>
        )}
        {isThisMonthOpen && (
          <Modal
            title="Bills in this month"
            mobileSize="full"
            closeHandler={handleThisMonthClick}
          >
            <TableWithPagination
              tableObj="bills"
              localDB={true}
              pageSize={10}
              totalCalculate={true}
            ></TableWithPagination>
          </Modal>
        )}
      </AnimatePresence>
    </>
  );
};

export default ButtonComponent;
