'use client';

import { useState } from 'react';
import { addComment } from '@/app/api/qna';

export default function CommentForm({ qnaId }) {
    const [comment, setComment] = useState('');
    const [submitting, setSubmitting] = useState(false);

    const submit = async (e) => {
        e.preventDefault();
        const trimmed = comment.replace(/^[\s"']+|[\s"']+$/g, '').trim();
        if (!trimmed) return;

        try {
            setSubmitting(true);
            const res = await addComment(qnaId, trimmed);
            setComment('');
            // if (onSuccess) await onSuccess();
        } catch (e) {
            alert('댓글 작성 실패');
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <div>
            <textarea
            placeholder = "댓글을 입력하세요"
            value={comment}
            onChange={(e) => setComment(e.target.value)}
            disabled={submitting}
            />
            <br />
            <button type="button" onClick={(e) => submit(e)} disabled={submitting}>
            게시
            </button>
        </div>
    );
}
