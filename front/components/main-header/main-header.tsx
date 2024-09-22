"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import NavLink from "./navlink";
import Image from "next/image";
import { fetchLoggedInUser, loggedInUser } from "@/api/userAPICalls";
import { StaticImport } from "next/dist/shared/lib/get-img-props";
import { UserInfo } from "os";

interface UserProfile {
  id: number | 0,
  userId: string | null,
  name: string | null,
  email: string | null,
  role: string | null,
  imageUrl: string | StaticImport | undefined,
  userStatus: string | null,
}

export default function MainHeader() {

  const [userInfo, setUserInfo] = useState<UserProfile | null>(null);

  useEffect(() => {
    async function fetchData() {
      const userDetails = await loggedInUser();
      setUserInfo(userDetails);
    }
    fetchData();
  }, []);
  // const userDetails = await loggedInUser();
  // console.log('userDetails??:::', userDetails);
  const userId = userInfo?.userId;
  console.log('userId???:::', userId);

  return (
    <header className="flex justify-between items-center px-6 py-4 bg-white shadow-md">
      <div className="flex items-center">
        <Link href="/" className='text-2xl font-semibold'>
          Biddy
        </Link>
      </div>

      <nav className="flex space-x-2 text-gray-700 font-semibold">
        <NavLink href="/">Home</NavLink>
        <NavLink href="/about">About</NavLink>
        <NavLink href="/post">Post</NavLink>
      </nav>

      <div className="rounded-full overflow-hidden">
        {userInfo?.userId ? (
          <Link href={`/${userInfo.userId}`} className="text-black">
            userId
            {/* <Image src={imageUrl} alt='profile picture' width={40} height={40} className="rounded-full" /> */}
          </Link>
        ) : (
          <nav className="flex text-gray-700 font-semibold">
            <NavLink href='/login'>Login</NavLink>
          </nav>
        )}
      </div>
    </header>
  )
}