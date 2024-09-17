'use server';

type FormState = {
  message: string;
}

export async function placeBidAction(prevState: FormState, formData: FormData) {

  // process the data
  console.log(prevState);
  console.log('formData', formData);
}