"use client";

import { useMemo, useState, useEffect } from "react";
import { requestAuthCode, validateAuthCode } from "@/app/api/auth";

const GMAIL_REGEX = /^[A-Za-z0-9._%+-]+@gmail\.com$/;

const validate = (email) => {
  if (!GMAIL_REGEX.test(email)) {
    return { ok: false, message: "Gmail 계정만 사용할 수 있습니다." };
  }
  return { ok: true, message: "" };
};

export default function EmailAuth({ email, setEmail, onVerified }) {
  const [code, setCode] = useState("");
  const [status, setStatus] = useState("");
  const [message, setMessage] = useState("idle");
  const [timeLeft, setTimeLeft] = useState(0);

  const emailRule = useMemo(() => validate(email), [email]);

  useEffect(() => {
    if (timeLeft > 0) {
      const timer = setTimeout(() => {
        setTimeLeft(timeLeft - 1);
      }, 1000);
      return () => clearTimeout(timer);
    }
  }, [timeLeft]);

  const sendCode = async () => {
    setMessage("");

    if (!emailRule.ok) return alert(emailRule.message);

    try {
      await requestAuthCode(email);
      setStatus("sent");
      setMessage("인증 코드가 전송되었습니다");
      setTimeLeft(300); // 5분 (300초)
      setCode(""); // 코드 초기화
    } catch (e) {
      setStatus("sent"); // 에러가 나도 입력란은 보이도록
      setMessage(e.message);
    }
  };

  const resendCode = async () => {
    await sendCode();
  };

  const verifyCode = async () => {
    setMessage("");
    try {
      const message = await validateAuthCode(email, code);
      setStatus("verified");
      setMessage(message);
      onVerified(true);
      setTimeLeft(0);
    } catch (e) {
      setStatus("sent");
      setMessage(e.message);
    }
  };

  const formatTime = (seconds) => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins}:${secs.toString().padStart(2, "0")}`;
  };

  return (
    <div className="space-y-3">
      <label className="block text-sm font-medium text-slate-700">이메일</label>
      <div className="flex gap-2">
        <input
          placeholder="이메일을 입력하세요"
          value={email}
          onChange={(e) => {
            setEmail(e.target.value);
            if (status === "sent") {
              // 이미 인증코드를 보낸 상태에서 이메일을 변경하면 상태 초기화
              setStatus("");
              setCode("");
              setTimeLeft(0);
            }
            onVerified(false);
          }}
          disabled={status === "sent" || status === "verified"}
          className={`flex-1 px-4 py-2 border border-slate-300 rounded-lg focus:outline-none transition-all
    ${
      status === "sent" || status === "verified"
        ? "bg-gray-200 cursor-not-allowed"
        : "focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
    }`}
        />
        <button
          type="button"
          onClick={sendCode}
          disabled={!email}
          className="px-4 py-2 bg-indigo-600 text-white rounded-lg font-medium hover:bg-indigo-700 transition-colors duration-200 disabled:bg-slate-300 disabled:cursor-not-allowed shadow-sm whitespace-nowrap"
        >
          인증코드 보내기
        </button>
      </div>
      {/* {status === "sent" && ( */}
      <div className="space-y-3 pt-2 border-t border-slate-200">
        <div className="flex gap-2 items-center">
          <div className="relative flex-1">
            <input
              placeholder="인증 코드"
              value={code}
              onChange={(e) => setCode(e.target.value)}
              disabled={status !== "sent"}
              className={`w-full px-4 py-2 pr-20 border border-slate-300 rounded-lg transition-all text-left
    ${
      status !== "sent"
        ? "bg-gray-200 cursor-not-allowed"
        : "bg-white focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
    }`}
            />
            {timeLeft > 0 && (
              <div className="absolute right-3 top-1/2 -translate-y-1/2 text-sm text-slate-500 font-medium">
                {formatTime(timeLeft)}
              </div>
            )}
          </div>
          <button
            type="button"
            onClick={verifyCode}
            disabled={status !== "sent" || !code.trim()}
            className="px-4 py-2 bg-emerald-600 text-white rounded-lg font-medium hover:bg-emerald-700 transition-colors duration-200 shadow-sm whitespace-nowrap disabled:bg-slate-300 disabled:cursor-not-allowed"
          >
            확인
          </button>
          <button
            type="button"
            onClick={resendCode}
            disabled={status === "" || status === "verified" || timeLeft > 0}
            className="px-4 py-2 bg-slate-100 text-slate-700 rounded-lg font-medium hover:bg-slate-200 transition-colors duration-200 whitespace-nowrap disabled:bg-slate-50 disabled:text-slate-400 disabled:cursor-not-allowed"
          >
            재전송
          </button>
        </div>

        {message && (
          <div
            className={`text-sm px-3 py-2 rounded-lg ${
              status === "verified"
                ? "bg-emerald-50 text-emerald-700 border border-emerald-200"
                : status === "error"
                ? "bg-red-50 text-red-700 border border-red-200"
                : "bg-blue-50 text-blue-700 border border-blue-200"
            }`}
          >
            {message}
          </div>
        )}
      </div>
      {/* // )} */}
    </div>
  );
}
