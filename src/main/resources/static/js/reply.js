async function get1(bno){ /*매개변수로 받은 bno에 등록된 댓글들을 반환*/

    const result = await axios.get(`/replies/list/${bno}`)

    // console.log(result);

    return result.data
}
/*async : 비동기 선언, await : async내부 비동기 호출부분에 사용*/