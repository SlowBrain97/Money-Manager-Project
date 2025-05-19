
import axiosInstance from "@/utils/axiosConfig";
import { db } from "@/utils/storeDb";
import { useEffect, useState } from "react";
interface UsePaginationProps{
    pageSize?:number;
    tableObj:string;
    localDB:boolean;
}
const usePagination = <P extends object>({tableObj,pageSize = 10,localDB}:UsePaginationProps) => {
    const [totalPages,setTotalPages] = useState<number>(0);
    const [currentPage,setCurrentPage] = useState<number>(1);
    const [loading,setLoading] = useState(false);
    const [error,setError] = useState<string | null>("");
    const [data,setData] = useState<P[] | undefined>([]);
    const goToPage = (page:number) => {
        setCurrentPage(page);
    }
    const setNextPage = () => {
        setCurrentPage((currentPage) => Math.min(currentPage+1, totalPages));
    }
    const setPreviousPage = () => {
        setCurrentPage(currentPage => Math.max(currentPage -1 , 1))
    }
    const fetchApi = async () => {
        setLoading(true);
        try {
            let receivedData; let totalItems;
            if (localDB) {
                receivedData = await db.getAllOfThisMonthWithPagination(tableObj,currentPage,pageSize)
                totalItems =  await db.getTotalItems(tableObj);
            }
            else {
                const response = await axiosInstance.get(`/${tableObj}?page=${currentPage}&size=${pageSize}`)
                receivedData = response.data;
                totalItems =  response.headers['totalItems'];
                
            }
            console.log(receivedData);
            setData(receivedData); setTotalPages(Math.ceil(totalItems/pageSize));
        }
         catch (error) {
            setError(`Error fetching data : ${error}`);
        }
        finally{
            setLoading(false);
        }
        
    }
    

    useEffect(()=> {
        fetchApi();
    // eslint-disable-next-line react-hooks/exhaustive-deps
    },[currentPage])




    return {currentPage,setNextPage,setPreviousPage,goToPage, loading, error, data,totalPages};
}

export default usePagination;