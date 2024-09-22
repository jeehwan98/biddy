'use server';

import { loginAPI } from "@/api/authAPICalls";
import { revalidatePath } from "next/cache";
import { redirect } from "next/navigation";

export async function loginAction(prevState: null, formData: FormData) {

  const userId = formData.get('userId') as string;
  const password = formData.get('password') as string;

  const loginDetails = {
    userId,
    password
  };

  if (!formData) {
    return {
      errors: {
        error: 'Invalid form data'
      }
    };
  }

  function isInvalidText(text: string | null): boolean {
    return !text || text.trim() == '';
  }

  if (
    isInvalidText(userId) ||
    isInvalidText(password)
  ) {
    return {
      message: 'Invalid input', formData
    };
  }

  return loginAPI(loginDetails);
}