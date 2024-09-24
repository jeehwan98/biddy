'use server';

import { loginAPI, registerUserAPI } from "@/api/authAPICalls";
import { uploadImage } from "@/lib/cloudinary";
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

export async function registerAction(prevState: null, formData: FormData) {

  if (!formData) {
    return { message: 'Invalid formdata', formData: null };
  }

  const userId = formData.get('userId') as string;
  const password = formData.get("password") as string;
  const imageUrl = formData.get("imageUrl") as File;
  const username = formData.get("username") as string;

  // check whether the inputted boxes were inputted correctly
  function isInvalidText(text: string | null): boolean {
    return !text || text.trim() === '';
  }

  let errors: {
    userId: string;
    password: string;
    username: string;
    imageUrl: File;
  } = {};

  if (isInvalidText(userId)) {
    errors.userId = 'User ID is required';
  };

  if (isInvalidText(password)) {
    errors.password = 'Invalid password';
  };

  if (isInvalidText(username)) {
    errors.username = 'Invalid username';
  }

  if (Object.keys(errors).length > 0) {
    return { errors };
  };

  let imageToUpload;

  try {
    imageToUpload = await uploadImage(imageUrl);
  } catch (error) {
    throw error;
  }

  const user = {
    userId,
    username,
    password,
    imageUrl
  };

  const registerUser = await registerUserAPI(user);
  console.log(registerUser);

}