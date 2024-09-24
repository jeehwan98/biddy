'use client';

import { bidAction } from "@/actions/bid-action";
import { useFormState } from "react-dom"

export default function HomePage() {

  const [formState, formAction] = useFormState(bidAction, {});

  return (
    <main className="p-20 text-black min-h-screen bg-gray-50">
      <form action={formAction}>
        <input name="bid" type="number" placeholder="Bid" />
        <button type="submit">Place Bid</button>
      </form>
    </main>
  )
}