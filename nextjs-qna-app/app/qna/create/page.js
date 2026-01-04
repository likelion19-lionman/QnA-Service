"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { query } from "@/app/api/qna";

export default function CreateQnaPage() {
  const router = useRouter();

  const [title, setTitle] = useState("");
  const [comment, setComment] = useState("");
  const [submitting, setSubmitting] = useState(false);

  const submit = async () => {
    if (!title.trim()) return alert("제목을 입력하세요.");
    if (!comment.trim()) return alert("내용을 입력하세요.");

    try {
      if (!confirm("등록 후에는 수정이 불가합니다. 게시하시겠습니까?")) {
        console.log("게시 취소됨");
        return;
      }
      setSubmitting(true);
      console.log("1");
      const res = await query(title, comment);
      console.log("2");
      alert("게시글이 등록되었습니다.");
      setTitle("");
      setComment("");

      router.push(`/qna/${res.id}`);
    } catch (e) {
      alert(e.detail)
    } finally {
      setSubmitting(false);
    }
  };

  const cancel = () => {
    router.push("/qna");
  };

  return (
    <div className="min-h-screen bg-slate-50 py-8 px-4">
      <div className="max-w-3xl mx-auto">
        <div className="bg-white rounded-lg shadow-sm border border-slate-200 p-8 space-y-6">
          <div className="space-y-2">
            <h1 className="text-3xl font-bold text-slate-800">새 질문 작성</h1>
            <p className="text-slate-600 text-sm">질문을 작성해주세요</p>
          </div>

          <div className="space-y-4">
            <div className="space-y-2">
              <label className="block text-sm font-medium text-slate-700">
                제목
              </label>
              <input
                placeholder="제목을 입력하세요"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                className="w-full px-4 py-3 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition-all"
              />
            </div>

            <div className="space-y-2">
              <label className="block text-sm font-medium text-slate-700">
                내용
              </label>
              <textarea
                placeholder="내용을 입력하세요"
                value={comment}
                onChange={(e) => setComment(e.target.value)}
                rows={12}
                className="w-full px-4 py-3 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition-all resize-none"
              />
            </div>

            <div className="flex gap-3 pt-4">
              <button
                type="button"
                onClick={submit}
                disabled={submitting}
                className="px-6 py-3 bg-indigo-600 text-white rounded-lg font-medium hover:bg-indigo-700 transition-colors duration-200 shadow-sm disabled:bg-slate-300 disabled:cursor-not-allowed"
              >
                {submitting ? "게시 중..." : "게시"}
              </button>
              <button
                type="button"
                onClick={cancel}
                disabled={submitting}
                className="px-6 py-3 bg-slate-600 text-white rounded-lg font-medium hover:bg-slate-700 transition-colors duration-200 shadow-sm disabled:bg-slate-300 disabled:cursor-not-allowed"
              >
                취소
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
