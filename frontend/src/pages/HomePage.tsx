import React from "react";
import ButtonComponent from "@/components/common/ButtonComponent";
import useGetDate from "@/hooks/useGetDate";
const HomePage: React.FC = () => {
  const { daysOfWeek, thisDay } = useGetDate();

  return (
    <>
      <div className="bg-color-yellow rounded-lg p-responsive-padding">
        <h1 className="text-responsive-h1 mb-4">
          Manage <br /> your money
        </h1>
        <div className="grid grid-cols-7 gap-x-[5%]">
          {daysOfWeek.map(({ day, date }, index) => (
            <div
              className={`flex justify-center items-center flex-col ${
                thisDay === date &&
                "bg-color-backGround rounded-2xl py-2 px-2 sm:px-5"
              }`}
              key={index}
            >
              <p
                className={
                  thisDay === date
                    ? "text-white font-bold"
                    : "font-normal text-gray-600"
                }
              >
                {day}
              </p>
              <p
                className={
                  thisDay === date ? "text-white font-bold" : "font-normal"
                }
              >
                {date}
              </p>
            </div>
          ))}
        </div>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 mt-4">
        <div className="col-span-1">
          <ButtonComponent />
        </div>
      </div>
    </>
  );
};

export default HomePage;
