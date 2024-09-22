'use client';

import { loginAction } from "@/actions/auth-actions";
import { useFormState } from "react-dom"

export default function LoginForm() {

  const [formState, formAction] = useFormState(loginAction, { errors: {} });

  return (
    <form action={formAction}>
      <div className="mb-4">
        <input
          type="text"
          id="userId"
          name="userId"
          className="w-full p-3 border rounded focus:outline-none focus:ring focus:border-blue-300"
          placeholder="UserId"
        />
      </div>
      <div className="mb-4">
        <input
          type="password"
          id="password"
          name="password"
          className="w-full p-3 border rounded focus:outline-none focus:ring focus:border-blue-300"
          placeholder="Password"
        />
      </div>
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
        <a href="#" className="text-sm text-blue-400 hover:text-blue-600">Register</a>
      </div>
    </form>
  )
}