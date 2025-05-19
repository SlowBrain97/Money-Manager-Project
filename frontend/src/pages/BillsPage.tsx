import { OrcState } from "@/utils/StateConfig";

const BillsPage = () => {
  const orcText = OrcState((state) => state.orcResult);

  return <div className="bg-white">{orcText}</div>;
};

export default BillsPage;
