"use client";

import { fetchLoggedInUser } from "@/api/userAPICalls"
import { StaticImport } from "next/dist/shared/lib/get-img-props";
import { notFound } from "next/navigation";
import { useEffect, useState } from "react";

interface UserProfileProps {
  params: {
    userId: string
  }
}

interface UserProfile {
  id: number;
  userId: string;
  name: string;
  email: string;
  role: string;
  imageUrl: string | StaticImport | undefined;
  userStatus: string;
}

export default async function UserPage({ params }: UserProfileProps) {
  const userId = params.userId;
  // const [userInfo, setUserInfo] = useState<UserProfile | null>(null);

  // useEffect(() => {
  //   async function fetchUserData() {
  //     try {
  //       const userDetails = await fetchLoggedInUser(userId);
  //       setUserInfo(userDetails);
  //     } catch (error) {
  //       throw error;
  //     }
  //   }

  //   fetchUserData();
  // }, [])

  const userDetails = await fetchLoggedInUser(userId);

  if (!userDetails) {
    notFound();
  }

  return (
    <main className="p-20 text-black bg-gray-50">
      <div>{userId} Profile Page</div>
    </main>
  )
}