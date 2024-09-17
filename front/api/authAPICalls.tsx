import { revalidatePath } from "next/cache";
import { redirect } from "next/navigation";


interface LoginUserInfoProps {
  userId: string;
  password: string;
}

interface LoginResponse {
  role: string | null;
  message: string | null;
  token: string | null;
  failType: string | null;
}

export async function loginAPI(userInfo: LoginUserInfoProps) {

  try {
    const response = await fetch(`${process.env.LOGIN_API_BASE_URL}/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': '*/*'
      },
      body: JSON.stringify(userInfo),
      credentials: 'include'
    });

    const responseData: LoginResponse = await response.json();
    const failedMessage = responseData.failType;
    const successMessage = responseData.message;

    if (failedMessage) {
      return failedMessage;
    }

    if (successMessage == 'login success') {
      revalidatePath('/');
      redirect('/');
    }

  } catch (error) {
    throw error;
  }
}