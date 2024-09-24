'use server';

type FormState = {
  message: string;
}

export async function bidAction(prevState: FormState, formData: FormData) {

  console.log(formData.get('bid') as string);
}