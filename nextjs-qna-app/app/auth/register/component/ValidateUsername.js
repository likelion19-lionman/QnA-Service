"use client";

import { useMemo, useState } from "react";
import { checkDuplication } from "@/app/api/auth";

const USERNAME_REGEX = /^[a-zA-Z0-9]{4,}$/;
// a -z + A-Z + 0~9 -> 4글자 이상

const validate = (username) => {
  if (!USERNAME_REGEX.test(username)) {
    return { ok: false, message: "아이디는 영문/숫자만 입력해주십시오" };
  }
  return { ok: true, message: "" };
};

export default function ValidateUsername({ username, setUsername, onChecked }) {
  const [fixed, setFixed] = useState(false);
  const usernameRule = useMemo(() => validate(username), [username]);

  const checkUsername = async () => {
    if (!usernameRule.ok) return alert("아이디를 입력해주세요.");

    try {
      const available = await checkDuplication(username);

      if (available) {
        alert("가입 가능한 아이디입니다.");
        onChecked?.(true);
        setFixed(fixed || true);
      } else {
        alert("이미 존재하는 아이디입니다.");
        onChecked?.(false);
      }
    } catch (e) {
      alert(e.message ?? "중복 확인 실패");
      onChecked?.(false);
    }
  };

  return (
    <div className="space-y-3">
      <label className="block text-sm font-medium text-slate-700">
        사용자명
      </label>
      <div className="flex gap-2">
        <input
          placeholder="사용자명을 입력하세요"
          value={username}
          onChange={(e) => {
            if (fixed) return;
            setUsername(e.target.value);
            onChecked?.(false);
          }}
          readOnly={fixed}
          disabled={fixed}
          className="flex-1 px-4 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition-all"
        />
        <button
          type="button"
          onClick={checkUsername}
          disabled={!usernameRule.ok}
          className="px-4 py-2 bg-indigo-600 text-white rounded-lg font-medium hover:bg-indigo-700 transition-colors duration-200 disabled:bg-slate-300 disabled:cursor-not-allowed shadow-sm whitespace-nowrap"
        >
          중복체크
        </button>
      </div>

      {username.length > 0 && username.length < 4 && (
        <div className="text-sm text-amber-600 bg-amber-50 border border-amber-200 rounded-lg px-3 py-2">
          아이디는 최소 4글자 이상이어야 합니다.
        </div>
      )}

      {username.length > 0 && !usernameRule.ok && (
        <div className="text-sm text-red-600 bg-red-50 border border-red-200 rounded-lg px-3 py-2">
          {usernameRule.message}
        </div>
      )}
    </div>
  );
}
