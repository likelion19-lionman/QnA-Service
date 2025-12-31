'use client'

export default function QnaPage({ params }) {
    return <></>;
    // const {qnaId} = params;
    // const [qna, setQna] = useState(null);
    // const [loading, setLoading] = useState(true);

    // useEffect(() => {
    //     const loadQna = async () => {
    //         try {
    //             setLoading(true);
    //             const data = await retrieveQna(qnaId);
    //             setQna(data);
    //         } catch (e) {
    //             console.error('QnA 로딩 실패:', e);
    //             alert(e.message || 'QnA를 불러오는데 실패했습니다.');
    //         } finally {
    //             setLoading(false);
    //         }
    //     };

    //     if (qnaId) {  // qnaId가 있을 때만 호출
    //         loadQna();
    //     }
    // }, [qnaId]);

    // const hasAnswers = Array.isArray(qna?.comments) && qna.comments.length > 0;

    // if (loading) return <div>읽어오는 중</div>
    // if (!qna) return <div>존재하지 않은 게시물입니다.</div>
   
    // return (
    //     <div>
    //         <h1>제목 : {qna.title}</h1>
    //         <p>내용 : {qna.content}</p>
    //         <div>작성자 : {qna.username}</div>

    //         {hasAnswers ? <CommentForm comments={qna?.comments}/> :
    //             <CommentForm
    //                 qnaId={qnaId} 
    //                 onSuccess={loadQna}
    //             />
    //         }
    //     </div>
    // )
}