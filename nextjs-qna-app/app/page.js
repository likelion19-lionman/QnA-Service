'use client'

import { useRouter } from "next/navigation"


export default function Home() {
    
  const router = useRouter();
    

    return (
      <dev>
        <button
          type="button"
          onClick={()=>router.push("/auth/login")}
          >로그인</button>

        <button
          type="button"
          onClick={()=>router.push("/auth")}
        >회원가입</button>
      </dev>
        )


}