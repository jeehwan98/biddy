import { NextRequest, NextResponse } from "next/server";

export async function middleware(req: NextRequest) {
  const token = req.cookies.get('token');

  if (!token) {
    return NextResponse.redirect('/login');
  }

  // validate the JWT here if necessary
  return NextResponse.next();
}