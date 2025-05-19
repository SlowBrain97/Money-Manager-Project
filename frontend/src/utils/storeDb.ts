import GetDate from "@/hooks/useGetDate";
import Dexie, {Table} from "dexie";


const {startThisMonth,endThisMonth} = GetDate();

export interface Bill {
    title?:string,
    amount:number,
    date:Date
    describe?: string
}
export class BillClass extends Dexie{
    bills!: Table<Bill,number>;
    constructor(){
        super("money-management");
        this.version(1).stores({
            bills: '++id,title,amount,date,describe'
        });
        this.bills = this.table('bills');
    }
     async getAllOfThisMonthWithPagination(table:string,page:number,pageSize:number){
        const tableObj = this.table(table)
        return await tableObj
        .where('date')
        .between(startThisMonth, endThisMonth, true, true)
        .offset((page - 1) * pageSize)
        .limit(pageSize)
        .toArray();
    }
    async addBillForThisMonth(bill:Bill){
        return await this.bills.add(bill);
    }
    async getTotalItems (table:string){
        return await this.table(table).where('id').notEqual("").count();
    }
    async getAllBillsOfThisMonth(table:string){
        return await this.table(table).where('date').between(startThisMonth,endThisMonth,true,true).toArray();
    }
   
    async getAllBills(){

        return await this.bills.toArray();
    }
}

export const db = new BillClass();
