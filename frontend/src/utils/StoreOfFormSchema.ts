import GetDate from "@/hooks/useGetDate";
import {z} from "zod";


const {thisFullDay} = GetDate();
export const BillFormContent = () => {
    const billFields = [
        {
            name: "title",
            type: 'text' as const,
            placeholder: 'Write bill title if it is necessary',
            label : "Title",
        },
        {
            name: 'describe',
            type: 'textarea' as const,
            placeholder: 'Write bill describe if it is necessary',
            label : "Describe"
        },
        {
            name: 'amount',
            type:'number' as const,
            placeholder: 'Write bill amount',
            require:true,
            label:'Amount',

        },
        {
            name :'date',
            type :'date' as const,
            label: "Date",
            defaultValue: thisFullDay
        }
    ]

    const billSchema = z.object({
        date: z.coerce.date(),
        title: z.string().nullable(),
        describe: z.string().nullable(),
        amount : z.number(),
        
    })
    const defaultValue = {
        title: "",
        describe: "",
        amount: undefined,
        date : thisFullDay
    }
    return {billFields, billSchema,defaultValue};
}

export const LoginFormContent = () => {
    const LoginFields = [
            {
                name: "email",
                type: "email" as const,
                label: "Email",
                placeholder: "Input your username, example: abc123@gmail.com",
                required: true,
            },
            {
                name: "password",
                type: "password" as const,
                label : "Password",
                placeholder: "Input your password",
                required:true,
            }
    ]
        


    const LoginSchema = z.object({
        email: z.string().email().min(8),
        password: z.string().min(8).regex(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/,{
            message: "Password must be 8 letters include lowercase, uppercase, number"
        })
    })
    const defaultValue = {
        email: "",
        password: "",
    }
    return {LoginFields, LoginSchema,defaultValue};
}