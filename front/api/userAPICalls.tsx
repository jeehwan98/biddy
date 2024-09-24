const tokenInfo = 'BEARER eyJkYXRlIjoxNzI3MDg2OTc0NzQwLCJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJzdWIiOiJqZWVod2FuOTgiLCJyb2xlIjoiVVNFUiIsInVzZXJTdGF0dXMiOiJhY3RpdmUiLCJpbWFnZVVybCI6Imh0dHBzOi8vcmVzLmNsb3VkaW5hcnkuY29tL2RtYmVoc3F4MS9pbWFnZS91cGxvYWQvdjE3MjQxNjAwODIvbmV4dGpzLWNvdXJzZS1tdXRhdGlvbnMvZWYxcmh6MXBwNHBnaGJocHY2aHkuanBnIiwibmFtZSI6Iuq5gOyngO2ZmCIsImV4cCI6MTcyNzEyMjk3NCwidXNlcklkIjoiamVlaHdhbjk4IiwiaWF0IjoxNzI3MDg2OTc0LCJlbWFpbCI6ImplZWh3YW5AZXhhbXBsZS5jb20ifQ.hbM1egbV5TFZkvdiWOWotvSje8sjEi4EaUsQxTLoOOw';

export async function loggedInUser() {
  try {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/user`, {
      method: 'GET',
      headers: {
        'Content-type': 'application/json',
        'Accept': '*/*',
        'Authorization': tokenInfo
      },
      credentials: 'include'
    });

    const responseData = await response.json();
    console.log(responseData.message);

    if (response.ok) {
      return responseData;
    }

  } catch (error) {
    throw error;
  }
}

export async function fetchLoggedInUser(userId: string) {

  try {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/${userId}`, {
      method: 'GET',
      headers: {
        'Content-type': 'application/json',
        'Accept': '*/*',
        'Authorization': tokenInfo
      }
    })

    const responseData = await response.json();
    if (response.ok) {
      return responseData;
    }
  } catch (error) {
    throw error;
  }
}