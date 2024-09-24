"use client";

import Link from "next/link";
import NavLink from "./navlink";
import { StaticImport } from "next/dist/shared/lib/get-img-props";
import RightLinkHeader from "./right-link";

export default function Header() {

  return (
    <header className="flex justify-between items-center px-6 py-4 bg-white shadow-xl">
      <div className="flex items-center">
        <Link href="/" className='text-black text-2xl font-semibold hover:scale-105'>
          Biddy
        </Link>
      </div>
      <nav className="flex space-x-2 text-gray-700 font-semibold">
        <NavLink href="/">Home</NavLink>
        <NavLink href="/about">About</NavLink>
        <NavLink href="/post">Post</NavLink>
      </nav>
      <RightLinkHeader />
    </header>
  )
}