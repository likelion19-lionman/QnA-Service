'use client';

import { useState } from 'react';
import qna from '@/app/api/qna'; 

export default function AddComment({ qnaId, onSuccess }) {
  const [comment, setComment] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const submit = async () => {
    const trimmed = comment.trim();
    if (!trimmed) return;

    try {
      setSubmitting(true);
      await qna(qnaId, trimmed);          
      setComment('');
      if (onSuccess) await onSuccess();   
    } catch (e) {
      alert(e?.message ?? '댓글 작성 실패');
    } finally {
      setSubmitting(false);
    }
  };

  //if문으로 comment가 없을 때 이 리턴을 사용하고 있을 때는 데이터를 불러오는 걸로..?
  return (
    <div>
      <textarea
        placeholder="댓글을 입력하세요"
        value={comment}
        onChange={(e) => setComment(e.target.value)}
        disabled={submitting}
      />
      <br />
      <button type="button" onClick={submit} disabled={submitting}>
        게시
      </button>
    </div>
  );
}
