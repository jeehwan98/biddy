"use client";

import { useFormState } from "react-dom";
import InputField from "../input-field";
import InputImage from "../input-image";
import { registerAction } from "@/actions/auth-actions";

export default function RegisterForm() {

  const [formState, formAction] = useFormState(registerAction, {});

  return (
    <form action={formAction}>
      <InputImage label="사진 등록" name="imageUrl" />
      <InputField
        type="text"
        name="userId"
        placeholder="아이디"
      />
      <InputField
        type="text"
        name="username"
        placeholder="이름"
      />
      <InputField
        type="password"
        name="password"
        placeholder="비밀번호"
      />
      <button
        type="submit"
        className="w-full bg-red-600 text-white p-3 rounded hover:bg-red-700 transition-colors"
      >
        Register
      </button>
    </form>
  )
}