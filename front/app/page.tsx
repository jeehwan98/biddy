'use client';

import { placeBidAction } from "@/actions/placeBid-action";
import { useFormState } from "react-dom";

export default function HomePage() {

  const [formState, formAction] = useFormState(placeBidAction, null);
  console.log('place bid action', placeBidAction);

  return (
    <main>
      <form action={formAction}>
        <input
          name="bid"
          type="nuber"
          placeholder="Bid"
        />
        <button type="submit">Place Bid</button>
      </form>
    </main>
  );
}
