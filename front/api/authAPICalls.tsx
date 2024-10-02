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

const baseURL = process.env.NEXT_PUBLIC_API_BASE_URL;

export async function loginAPI(loginInfo: LoginUserInfoProps) {
  try {
    const response = await fetch(`${baseURL}/auth/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify(loginInfo)
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
      console.log(responseData);
      revalidatePath('/user');
      redirect('/');
      return null;
    }
  } catch (error) {
    throw error;
  }
}

interface RegisterUserProps {
  userId: string;
  username: string;
  password: string;
  imageUrl: File;
}

export async function registerUserAPI(user: RegisterUserProps) {
  try {
    const response = await fetch(`${process.env.LOGIN_API_BASE_URL}/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        // 'Accept': '*/*'
      },
      credentials: 'include',
      body: JSON.stringify(user),
    });

    const responseData = await response.json();

    if (response.ok) {
      if (responseData.message === 'user registered') {
        revalidatePath('/login');
        redirect('/login');
      }
    }

  } catch (error) {
    throw error;
  }

}