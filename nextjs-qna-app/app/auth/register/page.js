'use client';

import { useState } from "react";
import { useRouter } from "next/navigation";
import { register } from "@/app/api/auth";
import EmailAuth from "@/app/auth/register/component/EmailAuth";
import ValidatePW from "./component/ValidatePW";
import ValidateUsername from "./component/ValidateUsername";


export default function RegisterPage() {
  const router = useRouter();

  const [username, setUsername] = useState("");
  const [usernameVerified, setUsernameVerified] = useState(false);

  const [email, setEmail] = useState("");
  const [emailVerified, setEmailVerified] = useState(false);

  const [password, setPassword] = useState("");
  const [passwordValid, setPasswordValid] = useState(false);

  const submit = async (e) => {
    e.preventDefault();

    if (!usernameVerified) return alert("아이디 중복확인을 해주세요.");
    if (!emailVerified) return alert("이메일 인증을 완료해주세요.");
    if (!passwordValid) return alert("비밀번호를 확인해주세요.");

    try {
      const res = await register(username, emailVerified, email, password);

      if (res) {
        alert("회원가입이 완료되었습니다.");
        router.push("/");
      }
    } catch (e) {
      alert("회원가입에 실패하였습니다.");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center px-4 bg-slate-50 py-8">
      <div className="w-full max-w-md">
        <div className="bg-white rounded-lg shadow-sm border border-slate-200 p-8 space-y-6">
          <div className="text-center space-y-2">
            <h1 className="text-3xl font-bold text-slate-800">회원가입</h1>
            <p className="text-slate-600 text-sm">새 계정을 만드세요</p>
          </div>

          <form onSubmit={submit} className="space-y-6">
            <ValidateUsername
              username={username}
              setUsername={setUsername}
              onChecked={setUsernameVerified}
            />

            <EmailAuth
              email={email}
              setEmail={setEmail}
              onVerified={setEmailVerified}
            />

            <ValidatePW
              password={password}
              setPassword={setPassword}
              setPasswordValid={setPasswordValid}
            />

            <div className="flex justify-between">
              <button
                type="submit"
                className="w-full px-4 py-3 bg-indigo-600 text-white rounded-lg font-medium hover:bg-indigo-700 transition-colors duration-200 shadow-sm"
              >
                회원가입
              </button>
              <button type="button" onClick={() => router.push("/")}>
                홈으로
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
