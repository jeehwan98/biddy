import Image from "next/image";
import { useEffect, useState } from "react";
import { loggedInUser } from "@/api/userAPICalls";
import Link from "next/link";
import { StaticImport } from "next/dist/shared/lib/get-img-props";
import { imageLoader } from "@/lib/cloudinary";
import NavLink from "./navlink";

interface UserProfile {
  id: number;
  userId: string;
  name: string;
  email: string;
  role: string;
  imageUrl: string | StaticImport | undefined;
  userStatus: string;
}

export default function RightLinkHeader() {
  const [userInfo, setUserInfo] = useState<UserProfile | null>(null);

  useEffect(() => {
    async function fetchUserData() {
      const userDetails = await loggedInUser();
      setUserInfo(userDetails);
    }

    fetchUserData();
  }, []);

  const imageUrl = userInfo?.imageUrl;
  const userId = userInfo?.userId;

  return (
    <div className="rounded-full overflow-hidden">
      {userId ? (
        <>
          <Link href={`/${userId}`} className="text-black">
            <Image
              src={imageUrl}
              loader={imageLoader}
              alt='profile picture'
              width={50}
              height={50}
              className="rounded-full hover:scale-105"
            />
            <div>{userInfo.name}</div>
          </Link>
          <button>Logout</button>
        </>

      ) : (
        <nav className="flex text-gray-700 font-semibold">
          <NavLink href='/login'>Login</NavLink>
        </nav>
      )}
    </div>
  )
}