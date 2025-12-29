

export default function AnswerComments({comments}){

    const answer = comments.

    return (
         <div>
            <h5>답변</h5>
                {comments.map((c) => (
                    <div key={c.id}>
                        <b>{c.writer}</b>: {c.content}
                    </div>
                ))}
        </div>
  );
}