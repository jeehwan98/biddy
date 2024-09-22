const tokenInfo = 'BEARER eyJkYXRlIjoxNzI3MDA4OTc3MzI0LCJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJzdWIiOiJqZWVod2FuOTgiLCJyb2xlIjoiVVNFUiIsInVzZXJTdGF0dXMiOiJhY3RpdmUiLCJpbWFnZVVybCI6ImltYWdlVXJsfiIsIm5hbWUiOiLquYDsp4DtmZgiLCJleHAiOjE3MjcwNDQ5NzcsInVzZXJJZCI6ImplZWh3YW45OCIsImlhdCI6MTcyNzAwODk3NywiZW1haWwiOiJqZWVod2FuQGV4YW1wbGUuY29tIn0.hcmNNNzOBKQKTniNunZoGGoqU8Sg3rQshC1sAbmcepA';

export async function loggedInUser() {
  try {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/user`, {
      method: 'GET',
      headers: {
        'Content-type': 'application/json',
        'Accept': '*/*',
        'Authorization': tokenInfo
      }
    });

    const responseData = await response.json();
    if (response.ok) {
      console.log('asdasdasd', responseData);
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