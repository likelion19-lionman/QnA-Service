"use client";

import { use, useEffect, useState } from "react";
import { retrieveQna } from "@/app/api/qna";
import CommentForm from "./component/CommentForm";

export default function QnaDetailPage({ params }) {
  const { id } = use(params);
  const [comments, setComments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [forbidden, setForbidden] = useState(false);
  const [error, setError] = useState(null);

  const loadQna = async () => {
    try {
      setLoading(true);
      const data = await retrieveQna(id);
      setComments(data);
    } catch (e) {
      if (e?.status === 403) {
        setForbidden(true);
      } else {
        setError("존재하지 않는 질문입니다.");
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    console.log(id, "아아");
    if (id) loadQna();
  }, [id]);

  if (loading) return <p>불러오는 중...</p>;
  if (forbidden) return <p>이 질문을 열람할 권한이 없습니다.</p>;
  if (error) return <p>{error}</p>;
  if (!comments || comments.length === 0) return <p>질문 정보가 없습니다.</p>;

  /** ✅ 구조 분리 */
  const question = comments[0]; // 질문
  const answers = comments.slice(1); // 답변들

  return (
    <div>
      {/* 질문 영역 */}
      <h2>질문</h2>
      <div style={{ marginBottom: "20px" }}>
        <p>{question.comment}</p>
        <small>작성자: {question.username}</small>
      </div>

      {/* 답변 영역 */}
      <h3>답변</h3>
      {answers.length === 0 && <p>아직 답변이 없습니다.</p>}

      <ul>
        {answers.map((answer) => (
          <li key={answer.id} style={{ marginBottom: "8px" }}>
            <p>{answer.comment}</p>
            <small>작성자: {answer.user.username}</small>
          </li>
        ))}
      </ul>

      {/* 댓글 작성 */}
      <CommentForm qnaId={id} onSuccess={loadQna} />
    </div>
  );
}
