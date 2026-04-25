/*
 * Copyright 2020-present AO Raiffeisenbank
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package raiffeisen.sbp.sdk;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import raiffeisen.sbp.sdk.data.TestData;
import raiffeisen.sbp.sdk.data.TestUtils;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.out.NFC;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
public class BindNfcLinkTest {

    @Test
    void createNfcLinkWithExceptionTest() {
        String qrId = TestUtils.getRandomUUID();
        NFC nfc = new NFC(qrId);

        SbpException ex = assertThrows(SbpException.class, () -> TestUtils.CLIENT.bindNfcLink(nfc));
        assertEquals(String.format(TestData.QR_DRAFT_DOES_NOT_REGISTERED, qrId), ex.getMessage());
    }
}
