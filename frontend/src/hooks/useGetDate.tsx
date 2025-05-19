import dayjs from "dayjs";
import isoWeek from "dayjs/plugin/isoWeek";
const GetDate = () => {
  const thisDay = dayjs().format("DD");
  const startThisMonth = dayjs().startOf('month').toDate();
  const endThisMonth = dayjs().endOf('month').toDate();
  const thisYear = dayjs().format("YYYY");
  const thisFullDay = dayjs().format("YYYY-MM-DD");
  const daysOfWeek = [];
  dayjs.extend(isoWeek);
  const startOfWeek = dayjs().startOf("isoWeek");
  for (let i = 0; i < 7; i++) {
    const date = dayjs(startOfWeek).add(i, "days");
    daysOfWeek.push({ day: date.format("ddd"), date: date.format("DD") });
  }

  return { daysOfWeek, thisDay, thisFullDay, startThisMonth,endThisMonth, thisYear };
};

export default GetDate;
