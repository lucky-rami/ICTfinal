function formatNumber(number) {
    return number.toLocaleString();
}

$(function(){
    const token = localStorage.getItem("token");

    function mypageMain(){
        $(".my_order_tbl li:not(.my_order_tit)").remove();
        $("#heart_artwrap_ani h4:not(.h_tit)").remove();
        $("#heart_artwrap_goods h4:not(.h_tit)").remove();
        $("#heart_lst_goods").empty();
        $("#heart_lst_ani").empty();
        //최근 주문내역과 좋아요 내역(애니, 굿즈) 내역 가져와서 반복
        $.ajax({
            url: '/user/mypageMainList',
            type: 'POST',
            contentType: 'application/json',
            headers: {
                "Authorization": `Bearer ${token}`  // JWT 토큰을 Authorization 헤더에 포함
            },
            success: function(response) {
                // 최근 주문 데이터
                let currentOrderData = response.currentOrderData;
                let currentLikeGoodsData = response.currentLikeGoodsData;
                let currentLikeAniData = response.currentLikeAniData;

                // 최근 주문 for문
                if (response.currentOrderData && response.currentOrderData.length > 0) {
                    response.currentOrderData.forEach(function(currentOrderData, index){
                        const formattedAmount = formatNumber(currentOrderData.amount) + "원"; // 여기서 형식화된 금액을 변수로 저장
                        $(".my_order_tbl").append(`
                            <li>
                              <a class="my_tbl" href="/user/mypage_order_detail/${currentOrderData.order_idx}">
                                <div class="date">${currentOrderData.order_date}</div>
                                <div class="history">
                                  <div class="imgbx">
                                    <div class="imgin">
                                      <img src="http://192.168.1.180:8000/${currentOrderData.pro_image}" />
                                    </div>
                                  </div>
                                  <div class="prd_order">
                                    <div class="prd_order_flex">
                                      <div class="prd_order_in">
                                        <em class="elp">
                                          <span>${currentOrderData.orderName}</span>
                                        </em>
                                      </div>
                                    </div>
                                  </div>
                                </div>
                                <div class="num">${currentOrderData.orderId}</div>
                                <div class="amount">${formattedAmount}</div>
                              </a>
                            </li>
                        `)
                    })// 최근 주문 for문 종료
                }else {
                    $(".my_order_tbl").empty();
                    $(".my_order").append(`
                        <div class="order_list_none">
                            <p style="margin-top:100px">최근 주문 내역이 없습니다.</p>
                        </div>
                    `);
                }
                // 굿즈 좋아요 최신 5개
                if (response.currentLikeGoodsData && response.currentLikeGoodsData.length > 0) {
                    response.currentLikeGoodsData.forEach(function(currentLikeGoodsData, index){
                        $("#heart_lst_goods").append(`
                            <li>
                              <a href="/storeDetail/${currentLikeGoodsData.idx}" style="color: #5d5d5d">
                                <div class="imgbx">
                                  <div class="imgin">
                                    <img src="http://192.168.1.180:8000/${currentLikeGoodsData.thumImg}" />
                                  </div>
                                </div>
                                <p class="txtani">${currentLikeGoodsData.ani_title}</p>
                                <p class="txt">${currentLikeGoodsData.title}</p>
                              </a>
                            </li>
                        `)
                    })// 굿즈 좋아요 최신 5개 종료
                }else {
                    $("#heart_artwrap_goods").append(`
                        <div class="order_list_none">
                            <p>굿즈 좋아요 내역이 없습니다.</p>
                        </div>
                    `);
                }
                // 애니 좋아요 최신 5개
                if (response.currentLikeAniData && response.currentLikeAniData.length > 0) {
                    response.currentLikeAniData.forEach(function(currentLikeAniData, index) {
                        $("#heart_lst_ani").append(`
                            <li>
                              <a href="" style="color: #5d5d5d">
                                <div class="imgbx">
                                  <div class="imgin">
                                    <img src="http://192.168.1.180:8000/${currentLikeAniData.post_img}" />
                                  </div>
                                </div>
                                <p class="txtani" style="text-decoration:none">${currentLikeAniData.title}</p>
                              </a>
                            </li>
                        `);
                    });
                } else {
                    $("#heart_artwrap_ani").append(`
                        <div class="order_list_none">
                            <p>애니 좋아요 내역이 없습니다.</p>
                        </div>
                    `);
                }
            },
            error: function(error) {
                console.log('오류 발생: ' + error);  // 에러 메시지 자체 출력
            }
        })//ajax끝
    }
    mypageMain();

});//jquery 끝
