"use client";

import Image from "next/image";
import { ChangeEvent, useRef, useState } from "react";

interface InputtedFieldProps {
  label: string;
  name: string;
}

export default function InputImage({ label, name }: InputtedFieldProps) {

  const imageInput = useRef<HTMLInputElement>(null);
  const [uploadImage, setUploadImage] = useState<string | null>(null);

  function handleClick() {
    imageInput.current?.click();
  }

  function handleImageChange(event: ChangeEvent<HTMLInputElement>) {
    const file = event.target.files?.[0];
    if (!file) {
      setUploadImage(null);
      return;
    }

    const fileReader = new FileReader();
    fileReader.onload = () => {
      setUploadImage(fileReader.result as string);
    };
    fileReader.readAsDataURL(file);
  }

  return (
    <div className="flex justify-center items-center">
      <div className="flex flex-col gap-6 m-8 items-center">
        <div className="flex w-44 h-44 border-2 border-[#a4abb9] text-center justify-center items-center text-[#a4abb9] relative rounded-full mb-5 overflow-hidden">
          {!uploadImage && <p>Preview</p>}
          {uploadImage && (
            <Image
              className="object-cover"
              src={uploadImage}
              alt="preview of profile picture"
              fill
            />
          )}
        </div>
        <input
          className="hidden"
          type="file"
          name={name}
          accept="image/png, image/jpeg"
          ref={imageInput}
          onChange={handleImageChange}
        />
        <button
          // className="rounded bg-black text-white px-1 py-2"
          className="w-fit bg-red-600 text-white px-2 py-3 rounded hover:bg-red-700 transition-colors"
          type="button"
          onClick={handleClick}
        >
          {label}
        </button>
      </div>
    </div>
  )
}