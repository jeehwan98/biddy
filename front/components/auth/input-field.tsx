interface InputtedFieldProps {
  type: string;
  name: string;
  placeholder: string;
}

export default function InputField({ type, name, placeholder }: InputtedFieldProps) {
  return (
    <div className="mb-4">
      <input
        type={type}
        name={name}
        placeholder={placeholder}
        className="w-full p-3 border rounded focus:outline-none focus:ring focus:border-blue-300"
        required
      />
    </div>
  )
}