/* eslint-disable @typescript-eslint/no-explicit-any */
import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "../ui/form";
import { Input } from "../ui/input";
import { Checkbox } from "../ui/checkbox";
import { Textarea } from "../ui/textarea";
import { Select, SelectContent, SelectItem, SelectTrigger } from "../ui/select";
import { Switch } from "../ui/switch";
import Button from "./Button";

type FieldType =
  | "text"
  | "textarea"
  | "email"
  | "password"
  | "checkbox"
  | "select"
  | "number"
  | "switch"
  | "date";

interface FieldProps {
  name: string;
  type: FieldType;
  label: string;
  description?: string;
  placeholder?: string;
  option?: { value: string; label: string }[];
  required?: boolean;
  defaultValue?: any;
}

interface FormProps {
  fields: FieldProps[];
  formSchema: z.ZodType<any, any>;
  defaultValue: Record<string, any>;
  handleSubmit: (data: any) => void;
}

const CustomForm = ({
  fields,
  formSchema,
  defaultValue,
  handleSubmit,
}: FormProps) => {
  const form = useForm<z.infer<typeof formSchema>>({
    defaultValues: defaultValue,
    resolver: zodResolver(formSchema),
  });

  const renderField = (field: FieldProps) => {
    return (
      <FormField
        control={form.control}
        key={field.name}
        name={field.name}
        render={({ field: formField }) => (
          <FormItem>
            <FormLabel>
              {field.label}
              {field.required && "*"}
            </FormLabel>
            <FormControl>
              {(() => {
                switch (field.type) {
                  case "text":
                  case "email":
                    return (
                      <Input
                        {...formField}
                        type={field.type === "email" ? "email" : "text"}
                        placeholder={field.placeholder}
                      />
                    );
                  case "checkbox":
                    return (
                      <Checkbox
                        checked={formField.value}
                        onCheckedChange={formField.onChange}
                      />
                    );
                  case "number":
                    return (
                      <Input
                        type="number"
                        {...formField}
                        placeholder={field.placeholder}
                        onChange={(e) => {
                          const value = e.target.value
                            ? parseFloat(e.target.value)
                            : "";
                          formField.onChange(value);
                        }}
                      />
                    );
                  case "textarea":
                    return (
                      <Textarea
                        placeholder={field.placeholder}
                        {...formField}
                      />
                    );
                  case "password":
                    return (
                      <Input
                        type="password"
                        placeholder={field.placeholder}
                        {...formField}
                      />
                    );
                  case "select":
                    return (
                      <Select
                        value={formField.value}
                        onValueChange={formField.onChange}
                      >
                        <SelectTrigger>
                          {field.placeholder || "Select a item"}
                        </SelectTrigger>
                        <SelectContent>
                          {field.option?.map((item) => (
                            <SelectItem key={item.value} value={item.value}>
                              {item.label}
                            </SelectItem>
                          ))}
                        </SelectContent>
                      </Select>
                    );
                  case "switch":
                    return (
                      <Switch
                        checked={formField.value}
                        onCheckedChange={formField.onChange}
                      />
                    );
                  case "date": {
                    return (
                      <Input
                        type="date"
                        {...formField}
                        value={formField.value ?? ""}
                      />
                    );
                  }

                  default:
                    return <Input type="text" {...formField} />;
                }
              })()}
            </FormControl>
            <FormMessage className="text-red-500" />
          </FormItem>
        )}
      ></FormField>
    );
  };
  return (
    <Form {...form}>
      <form
        onSubmit={form.handleSubmit(handleSubmit)}
        className="space-y-2 p-3"
      >
        {fields.map(renderField)}
        <Button type="submit" variant={"default"} size={"default"}>
          Gửi
        </Button>
      </form>
    </Form>
  );
};

export default CustomForm;
