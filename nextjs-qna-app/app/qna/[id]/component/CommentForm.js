'use client';

import { useState } from 'react';
import { addComment } from '@/app/api/qna';

export default function CommentForm({ qnaId, onSuccess }) {
    const [comment, setComment] = useState('');
    const [submitting, setSubmitting] = useState(false);

    const submit = async (e) => {
        e.preventDefault();

        const trimmed = comment.trim();
        if (!trimmed) return;

        try {
            setSubmitting(true);
            await addComment(qnaId, trimmed);
            setComment('');

            // ✅ 댓글 등록 후 부모에서 다시 조회
            if (onSuccess) {
                await onSuccess();
            }
        } catch (e) {
            alert('댓글 작성 실패');
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <div>
            <textarea
                placeholder="댓글을 입력하세요"
                value={comment}
                onChange={(e) => setComment(e.target.value)}
                disabled={submitting}
            />
            <br />
            <button
                type="button"
                onClick={submit}
                disabled={submitting}
            >
                게시
            </button>
        </div>
    );
}
