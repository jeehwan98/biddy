'use client';

import { loginAction } from "@/actions/auth-actions";
import { useFormState } from "react-dom"
import InputField from "../input-field";
import Link from "next/link";

export default function LoginForm() {

  const [formState, formAction] = useFormState(loginAction, { errors: {} });

  return (
    <form action={formAction}>
      <InputField
        type="text"
        name="userId"
      />
      <InputField
        type="password"
        name="password"
      />
      {/* {formState.errors && <p className="text-red-600 mb-3">{formState.message}</p>} */}
      {formState?.errors && Object.keys(formState.errors || {}).length > 0 && (
        <ul>
          {Object.keys(formState.errors || {}).map((error) => (
            <li key={error} className="text-red-600 mb-4 mt-4 text-sm">{formState.errors[error]}</li>
          ))}
        </ul>
      )}
      <button
        type="submit"
        className="w-full bg-red-600 text-white p-3 rounded hover:bg-red-700 transition-colors"
      >
        Log in
      </button>
      <div className="flex justify-between items-center mt-3">
        <a href="#" className="text-sm text-blue-400 hover:text-blue-600">Forgot password?</a>
        <Link href="/register" className="text-sm text-blue-400 hover:text-blue-600">Register</Link>
      </div>
    </form>
  )
}