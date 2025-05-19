import {
  Pagination,
  PaginationContent,
  PaginationItem,
  PaginationNext,
  PaginationLink,
  PaginationPrevious,
} from "../ui/pagination";

interface PaginationProps {
  goToPage: (page: number) => void;
  setNextPage: () => void;
  setPreviousPage: () => void;
  totalPages: number;
  currentPage: number;
}

const CustomPagination = ({
  goToPage,
  setNextPage,
  setPreviousPage,
  totalPages,
  currentPage,
}: PaginationProps) => {
  const renderButton = () => {
    return Array.from({ length: totalPages }, (_, index) => {
      const pageNumber = index + 1;
      return (
        <PaginationItem key={index} className="hover-scale">
          <PaginationLink
            onClick={() => goToPage(pageNumber)}
            isActive={currentPage === pageNumber}
          >
            {pageNumber}
          </PaginationLink>
        </PaginationItem>
      );
    });
  };

  return (
    <Pagination className="p-8 mt-3">
      <PaginationContent className="">
        <PaginationItem className="hover-scale">
          <PaginationPrevious
            onClick={setPreviousPage}
            isActive={currentPage > 1}
          />
        </PaginationItem>
        {renderButton()}
        <PaginationItem className="hover-scale">
          <PaginationNext
            onClick={setNextPage}
            isActive={currentPage < totalPages}
          />
        </PaginationItem>
      </PaginationContent>
    </Pagination>
  );
};

export default CustomPagination;
