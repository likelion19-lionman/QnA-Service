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
            await addComment(qnaId, trimmed);
            setComment('');
        } catch (e) {
            alert('댓글 작성 실패');
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <form onSubmit={submit} className="space-y-4">
            {/* textarea */}
            <textarea
                placeholder="댓글을 입력하세요"
                value={comment}
                onChange={(e) => setComment(e.target.value)}
                disabled={submitting}
                rows={4}
                className="
          w-full
          resize-none
          px-4 py-3
          border border-slate-300
          rounded-lg
          text-slate-800
          placeholder:text-slate-400
          focus:outline-none
          focus:ring-2 focus:ring-indigo-500
          focus:border-transparent
          transition-all
          disabled:bg-slate-100
          disabled:text-slate-400
        "
            />

            {/* submit */}
            <div className="flex justify-end">
                <button
                    type="submit"
                    disabled={submitting}
                    className="
            px-4 py-2
            rounded-lg
            bg-indigo-600
            text-white
            font-medium
            hover:bg-indigo-700
            transition
            disabled:bg-indigo-300
            disabled:cursor-not-allowed
          "
                >
                    {submitting ? '게시 중...' : '게시'}
                </button>
            </div>
        </form>
    );
}
