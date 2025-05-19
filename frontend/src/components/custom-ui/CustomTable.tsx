import {
  Table,
  TableBody,
  TableCell,
  TableRow,
  TableCaption,
  TableHead,
  TableHeader,
} from "../ui/table";
import usePagination from "@/hooks/usePagination";
import Loading from "./Loading";
import Error from "./Error";
import CustomPagination from "./CustomPagination";

interface TableWithPaginationProps {
  totalCalculate: boolean;
  caption?: string;
  pageSize?: number;
  tableObj: string;
  localDB: boolean;
}

const TableWithPagination = <P extends object>({
  totalCalculate,
  caption,
  pageSize,
  tableObj,
  localDB,
}: TableWithPaginationProps) => {
  const { data, loading, error, ...props } = usePagination<P>({
    tableObj,
    pageSize,
    localDB,
  });

  if (loading) return <Loading />;
  if (error) return <Error />;

  return (
    <Table className="table-caption w-[90%] mx-auto">
      {caption && <TableCaption>{caption}</TableCaption>}
      <TableHeader>
        <TableRow>
          {Object.keys(data?.[0] || {}).map((head, index) => (
            <TableHead key={index}>{head.toUpperCase()}</TableHead>
          ))}
        </TableRow>
      </TableHeader>
      <TableBody className="mb-4">
        {data?.map((item, index) => (
          <TableRow key={index}>
            {Object.keys(item).map((key) => (
              <TableCell key={key}>
                {item[key as keyof P] instanceof Date
                  ? (item[key as keyof P] as Date).toLocaleDateString("en-GB", {
                      day: "2-digit",
                      month: "2-digit",
                      year: "numeric",
                    })
                  : String(item[key as keyof P])}
              </TableCell>
            ))}
          </TableRow>
        ))}
      </TableBody>
      {totalCalculate && (
        <TableRow className="font-bold">
          <TableCell>Total</TableCell>
          <TableCell>
            {(() => {
              return data?.reduce((acc, item) => {
                // eslint-disable-next-line @typescript-eslint/no-explicit-any
                const value = (item as Record<string, any>)["amount"];
                return (acc += value);
              }, 0);
            })()}
          </TableCell>
        </TableRow>
      )}

      <CustomPagination {...props} />
    </Table>
  );
};

export default TableWithPagination;
