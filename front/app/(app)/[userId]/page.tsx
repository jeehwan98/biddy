import { fetchLoggedInUser } from "@/api/userAPICalls"
import { notFound } from "next/navigation";

interface UserProfileProps {
  params: {
    userId: string
  }
}

export default async function UserPage({ params }: UserProfileProps) {

  const user = await fetchLoggedInUser(params.userId);
  console.log('🩷', user);

  if (!user) {
    notFound();
  }


  return (
    <main className="p-20 text-black min-h-screen bg-gray-50">{user.userId} Profile Page</main>
  )
}