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

export async function loginAPI(loginInfo: LoginUserInfoProps) {

  try {
    const response = await fetch(`${process.env.LOGIN_API_BASE_URL}/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': '*/*'
      },
      body: JSON.stringify(loginInfo),
      credentials: 'include'
    });

    const responseData: LoginResponse = await response.json();

    if (responseData.failType) {
      return {
        errors: {
          error: responseData.failType
        }
      };
    }

    if (responseData.message == 'login success') {
      console.log('responseData: ', responseData);
      revalidatePath('/user');
      redirect('/');
      return null;
    }
  } catch (error) {
    throw error;
  }
}