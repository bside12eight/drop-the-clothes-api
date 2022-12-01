package com.droptheclothes.api.model.entity;

import com.droptheclothes.api.model.entity.pk.ChargeId;
import com.droptheclothes.api.model.enums.ChargeReasonType;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@IdClass(ChargeId.class)
@NoArgsConstructor
@AllArgsConstructor
public class Charge {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articleId", referencedColumnName = "articleId")
    private Article article;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", referencedColumnName = "memberId")
    private Member member;

    @Enumerated(EnumType.STRING)
    private ChargeReasonType chargeReason;

    private LocalDateTime createdAt;
}
