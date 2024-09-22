import Link from "next/link";
import { usePathname } from "next/navigation";
import { ReactNode } from "react";

interface NavLinkProps {
  href: string;
  children: ReactNode;
}

export default function NavLink({ href, children }: NavLinkProps) {
  const path = usePathname();
  const isActive = href === '/' ? path === href : path.startsWith(href);
  return (
    <Link
      href={href}
      className={`px-3 py-2 ${isActive ? 'text-blue-500 font-bold' : 'text-gray-700 hover:text-blue-500'}`}
    >
      {children}
    </Link>
  );
}